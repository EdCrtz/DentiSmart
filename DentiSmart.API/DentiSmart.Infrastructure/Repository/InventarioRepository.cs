using DentiSmart.Domain.Contracts;
using DentiSmart.Domain.Models;
using DentiSmart.Infrastructure.DataBase;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Infrastructure.Repository
{
    public class InventarioRepository : IInventarioRepository
    {
        private readonly IMongoCollection<Inventario> _InventarioCollection;

        public InventarioRepository(IDentiSmartDatabaseSettings settings)
        {
            var mdbClient = new MongoClient(settings.ConnectionString);
            var database = mdbClient.GetDatabase(settings.DatabaseName);
            _InventarioCollection = database.GetCollection<Inventario>("Inventario");
        }

        public async Task<List<Inventario>> Get()
        {
            return await _InventarioCollection.Find(inventario => true).ToListAsync();
        }
       

        public async Task<Inventario> GetById(string id)
        {
            return await _InventarioCollection.Find(inventario => inventario.Id == id).FirstOrDefaultAsync();
        }

        public async Task<List<Inventario>> GetByConsultorio(string id)
        {
            return await _InventarioCollection.Find(inventario => inventario.Consultorio == id).ToListAsync();
        }

        public async Task<Inventario> Create(Inventario inventario)
        {
            await _InventarioCollection.InsertOneAsync(inventario);
            return inventario;
        }

        public async Task Update(Inventario nuevoinventario)
        {

            await _InventarioCollection.ReplaceOneAsync(inventario => inventario.Id == nuevoinventario.Id, nuevoinventario);
        }

        public async Task Delete(Inventario inventarioborrar)
        {
            await _InventarioCollection.DeleteOneAsync(inventario => inventario.Id == inventarioborrar.Id);
        }

        public async Task DeleteById(string id)
        {
            await _InventarioCollection.DeleteOneAsync(inventario => inventario.Id == id);
        }

     

    }
}
