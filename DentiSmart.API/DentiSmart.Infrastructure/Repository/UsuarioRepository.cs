using DentiSmart.Domain.Contracts;
using DentiSmart.Domain.Models;
using DentiSmart.Infrastructure.DataBase;
using Microsoft.Extensions.Configuration;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Infrastructure.Repository
{
    public class UsuarioRepository: IUsuarioRepository
    {
        private readonly IMongoCollection<Usuario> _usuarioCollection;
        private IConfiguration _config;
        private string _secretKey;
        private ConsultorioRepository _repositoryConsultorios;
        public UsuarioRepository(IDentiSmartDatabaseSettings settings, IConfiguration config)
        {
            var mdbClient = new MongoClient(settings.ConnectionString);
            var database = mdbClient.GetDatabase(settings.DatabaseName);
            _usuarioCollection = database.GetCollection<Usuario>("Usuario");
            _config = config;
            _repositoryConsultorios = new ConsultorioRepository(settings);
            _secretKey = _config.GetValue<string>("SecretKey");
        }

        public async Task<List<Usuario>> Get()
        {
            return await _usuarioCollection.Find(usuario => true).ToListAsync();
        }
        public async Task<List<Usuario>> GetByRole(string role)
        {
            var usuarios = await _usuarioCollection.Find(usuario => usuario.Role == role).ToListAsync();
          
            if (role.Equals("Dentista")) {
                foreach (Usuario usuario in usuarios)
                    usuario.DatosConsultorio =  await _repositoryConsultorios.GetById(usuario.Consultorio);
            }
            return usuarios;
        }
        public async Task<List<Usuario>> GetDentistaByConsultorio(string consultorio) {
            return await _usuarioCollection.Find(usuario => usuario.Role == "Dentista" && usuario.Consultorio == consultorio).ToListAsync();
        }
        public async Task<Usuario> GetById(string id)
        {
            return await _usuarioCollection.Find(usuario => usuario.Id == id).FirstOrDefaultAsync();
        }
        public async Task<Usuario> GetByUserName(string nombreUsuario)
        {
            return await _usuarioCollection.Find(usuario => usuario.NombreUsuario == nombreUsuario).FirstOrDefaultAsync();
        }
        public async Task<Usuario> Create(Usuario usuario)
        {
            usuario.Contrasenia = BCrypt.Net.BCrypt.HashPassword(usuario.Contrasenia);
            await _usuarioCollection.InsertOneAsync(usuario);
            return usuario;
        }

        public async Task Update(Usuario nuevoUsuario)
        {
            var usuarioNoModificado = await _usuarioCollection.Find(usuario => usuario.Id == nuevoUsuario.Id).FirstOrDefaultAsync(); 
            await _usuarioCollection.ReplaceOneAsync(usuario => usuario.Id == nuevoUsuario.Id, nuevoUsuario);
           
        }

        public async Task Delete(Usuario ususarioBorrar)
        {
            await _usuarioCollection.DeleteOneAsync(usuario => usuario.Id == ususarioBorrar.Id);
        }

        public async Task DeleteById(string id)
        {
            await _usuarioCollection.DeleteOneAsync(usuario => usuario.Id == id);
        }
    }
}
