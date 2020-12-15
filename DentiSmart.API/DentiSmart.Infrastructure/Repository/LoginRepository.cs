using DentiSmart.Domain.Contracts;
using DentiSmart.Domain.Models;
using DentiSmart.Infrastructure.DataBase;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using Microsoft.VisualBasic;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Infrastructure.Repository
{
    public class LoginRepository: ILoginRepository
    {
        private readonly IMongoCollection<LoginResult> _usuarioCollection;
        private readonly IMongoCollection<Usuario> _usuarioCollectionRegister;
        private LoginResult result;
        private IConfiguration _config;
        private string _secretKey;
        public LoginRepository(IDentiSmartDatabaseSettings settings, IConfiguration config)
        {
            var mdbClient = new MongoClient(settings.ConnectionString);
            var database = mdbClient.GetDatabase(settings.DatabaseName);
            _usuarioCollection = database.GetCollection<LoginResult>("Usuario");
            _usuarioCollectionRegister = database.GetCollection<Usuario>("Usuario");
            _config = config;
            _secretKey = _config.GetValue<string>("SecretKey");
        }

        public async Task<LoginResult> Login(LoginCredentials credentials)
        {
            result = await _usuarioCollection.Find(usuario =>usuario.NombreUsuario.Equals(credentials.NombreUsuario)
            ).FirstOrDefaultAsync();
            if (result != null && BCrypt.Net.BCrypt.Verify(credentials.Contrasenia, result.Contrasenia))
            {
                result.Token = BuildToken(result);
                result.Contrasenia = credentials.Contrasenia;
                return result;
            }
            return null;
        }

        public async Task<LoginResult> LoginFacebook(Usuario credentials)
        {
            result = await _usuarioCollection.Find(usuario => usuario.IdFacebook.Equals(credentials.IdFacebook)
            ).FirstOrDefaultAsync();
            if (result != null)
            {
                result.Token = BuildToken(result);
            }
            else {
                // Registr al compa en mongo
                await _usuarioCollectionRegister.InsertOneAsync(credentials);
                result = await _usuarioCollection.Find(usuario => usuario.IdFacebook.Equals(credentials.IdFacebook)).FirstOrDefaultAsync();
                result.Token = BuildToken(result);
            }
            return result;

        }

        public async Task<LoginResult> LoginGoogle(Usuario credentials)
        {
            result = await _usuarioCollection.Find(usuario => usuario.IdGoogle.Equals(credentials.IdGoogle)
            ).FirstOrDefaultAsync();
            if (result != null)
            {
                result.Token = BuildToken(result);
            }
            else
            {
                // Registr al compa en mongo
                await _usuarioCollectionRegister.InsertOneAsync(credentials);
                result = await _usuarioCollection.Find(usuario => usuario.IdGoogle.Equals(credentials.IdGoogle)).FirstOrDefaultAsync();
                result.Token = BuildToken(result);
            }
            return result;

        }
        private string BuildToken(LoginResult user)
        { 
            var key = Encoding.ASCII.GetBytes(_secretKey);
            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(new Claim[]
            {
                    new Claim("Id", user.Id.ToString()),
                    new Claim("Nombre", user.Nombre),
                    new Claim("ApellidoPat", user.ApellidoPat),
                    new Claim("ApellidoMat",user.ApellidoMat),
                    new Claim("NombreUsuario", user.NombreUsuario),
                    new Claim("Email", user.Email == null ? string.Empty : user.Email),
                    new Claim("Telefono", user.Telefono.ToString()),
                    new Claim(ClaimTypes.Role, user.Role)

            }),
                // Nuestro token va a durar un día
                Expires = DateTime.UtcNow.AddDays(1),
                // Credenciales para generar el token usando nuestro secretykey y el algoritmo hash 256
                SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature)
            };

            var tokenHandler = new JwtSecurityTokenHandler();
            var createdToken = tokenHandler.CreateToken(tokenDescriptor);
            return tokenHandler.WriteToken(createdToken);
        }
    }
}