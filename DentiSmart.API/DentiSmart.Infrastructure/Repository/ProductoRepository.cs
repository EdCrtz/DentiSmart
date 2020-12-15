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
    public class ProductoRepository : IProductoRepository
    {
        private readonly IMongoCollection<Producto> _productoCollection;

        public ProductoRepository(IDentiSmartDatabaseSettings settings)
        {
            var mdbClient = new MongoClient(settings.ConnectionString);
            var database = mdbClient.GetDatabase(settings.DatabaseName);
            _productoCollection = database.GetCollection<Producto>("Producto");
        }

        public async Task<List<Producto>> Get()
        {
            return await _productoCollection.Find(producto => true).ToListAsync();
        }
        public async Task<List<Producto>> GetByConsultorio(string consultorio)
        {
            return await _productoCollection.Find(producto => producto.Consultorio == consultorio).ToListAsync();
        }

        public async Task<Producto> GetById(string id)
        {
            return await _productoCollection.Find(producto => producto.Id == id).FirstOrDefaultAsync();
        }

        public async Task<Producto> Create(Producto producto)
        {
            await _productoCollection.InsertOneAsync(producto);
            return producto;
        }

        public async Task Update(Producto nuevoproducto)
        {

            await _productoCollection.ReplaceOneAsync(producto => producto.Id == nuevoproducto.Id, nuevoproducto);
        }

        public async Task Delete(Producto productoborrar)
        {
            await _productoCollection.DeleteOneAsync(producto => producto.Id == productoborrar.Id);
        }

        public async Task DeleteById(string id)
        {
            await _productoCollection.DeleteOneAsync(producto => producto.Id == id);
        }
    }
}
