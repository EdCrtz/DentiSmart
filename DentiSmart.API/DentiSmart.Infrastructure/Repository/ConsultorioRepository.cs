using DentiSmart.Domain.Models;
using DentiSmart.Infrastructure.DataBase;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Infrastructure.Repository
{
    public class ConsultorioRepository
    {
        private readonly IMongoCollection<Consultorio> _consultorioCollection;

        public ConsultorioRepository(IDentiSmartDatabaseSettings settings)
        {
            var mdbClient = new MongoClient(settings.ConnectionString);
            var database = mdbClient.GetDatabase(settings.DatabaseName);
            _consultorioCollection = database.GetCollection<Consultorio>(settings.ConsultorioCollectionName);
        }

        public async Task<List<Consultorio>> Get()
        {
            return await _consultorioCollection.Find(tienda => true).ToListAsync();
        }

        public async Task<Consultorio> GetById(string id)
        {
            return await _consultorioCollection.Find(consultorio => consultorio.Id == id).FirstOrDefaultAsync();
        }

        public async Task<Consultorio> Create(Consultorio consultorio)
        {
            await _consultorioCollection.InsertOneAsync(consultorio);
            return consultorio;
        }

        public async Task Update(Consultorio nueveConsultorio)
        {

            await _consultorioCollection.ReplaceOneAsync(consultorio => consultorio.Id == nueveConsultorio.Id, nueveConsultorio);
        }

        public async Task Delete(Consultorio consultorioborrar)
        {
            await _consultorioCollection.DeleteOneAsync(consultorio => consultorio.Id == consultorioborrar.Id);
        }

        public async Task DeleteById(string id)
        {
            await _consultorioCollection.DeleteOneAsync(consultorio => consultorio.Id == id);
        }
    }
}
