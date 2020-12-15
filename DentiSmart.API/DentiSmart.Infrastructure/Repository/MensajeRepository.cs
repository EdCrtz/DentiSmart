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
    public class MensajeRepository : IMensajeRepository
    {
        private readonly IMongoCollection<Mensajes> _MensajesCollection;
        private IConfiguration _config;
        private string _secretKey;
        public MensajeRepository(IDentiSmartDatabaseSettings settings, IConfiguration config)
        {
            var mdbClient = new MongoClient(settings.ConnectionString);
            var database = mdbClient.GetDatabase(settings.DatabaseName);
            _MensajesCollection = database.GetCollection<Mensajes>("Mensajes");
            //database.DropCollection("Inventario");
            _config = config;
            _secretKey = _config.GetValue<string>("SecretKey");
        }

        public async Task<List<Mensajes>> Get()
        {
            return await _MensajesCollection.Find(mensaje => true).ToListAsync();
        }
        public async Task<List<Mensajes>> GetByUserName(string username)
        {
            var mensajes = await _MensajesCollection.Find(mensaje => mensaje.UserNameUsuarioTransmisor == username || mensaje.UserNameUsuarioReceptor == username).ToListAsync();

       


            return mensajes;
        }
      
        public async Task<Mensajes> GetById(string id)
        {
            return await _MensajesCollection.Find(mensajes => mensajes.Id == id).FirstOrDefaultAsync();
        }
   
        public async Task<Mensajes> Create(Mensajes mensaje)
        {
            await _MensajesCollection.InsertOneAsync(mensaje);
            return mensaje;
        }


        public async Task DeleteById(string id)
        {
            await _MensajesCollection.DeleteOneAsync(mensajes => mensajes.Id == id);
        }
    }
}
