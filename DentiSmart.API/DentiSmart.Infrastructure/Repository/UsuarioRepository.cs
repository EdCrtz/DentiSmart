using DentiSmart.Domain.Models;
using DentiSmart.Infrastructure.DataBase;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Infrastructure.Repository
{
    public class UsuarioRepository
    {
        private readonly IMongoCollection<Usuario> _usuarioCollection;

        public UsuarioRepository(IDentiSmartDatabaseSettings settings)
        {
            var mdbClient = new MongoClient(settings.ConnectionString);
            var database = mdbClient.GetDatabase(settings.DatabaseName);
            _usuarioCollection = database.GetCollection<Usuario>("Usuario");
        }

        public async Task<List<Usuario>> Get()
        {
            return await _usuarioCollection.Find(usuario => true).ToListAsync();
        }

        public async Task<Usuario> GetById(string id)
        {
            return await _usuarioCollection.Find(usuario => usuario.Id == id).FirstOrDefaultAsync();
        }

        public async Task<Usuario> Create(Usuario usuario)
        {
            usuario.Contrasenia = CryptographyService.HashPasswordUsingPBKDF2(usuario.Contrasenia);
            await _usuarioCollection.InsertOneAsync(usuario);
            return usuario;
        }

        public async Task Update(Usuario nuevoUsuario)
        {
            nuevoUsuario.Contrasenia = CryptographyService.HashPasswordUsingPBKDF2(nuevoUsuario.Contrasenia);
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
