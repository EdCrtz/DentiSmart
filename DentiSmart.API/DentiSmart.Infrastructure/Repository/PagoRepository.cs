using DentiSmart.Domain.Models;
using DentiSmart.Infrastructure.DataBase;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Infrastructure.Repository
{
    public class PagoRepository: IPagoRepository
    {
        private readonly IMongoCollection<Pago> _pagoCollection;
        private readonly IMongoCollection<Cita> _citaCollection;

        public PagoRepository(IDentiSmartDatabaseSettings settings)
        {
            var mdbClient = new MongoClient(settings.ConnectionString);
            var database = mdbClient.GetDatabase(settings.DatabaseName);
            _pagoCollection = database.GetCollection<Pago>("Pago");
            _citaCollection = database.GetCollection<Cita>("Cita");
        }

        public async Task<List<Pago>> Get()
        {
            return await _pagoCollection.Find(pago => true).ToListAsync();
        }

        public async Task<Pago> GetById(string id)
        {
            return await _pagoCollection.Find(pago => pago.Id == id).FirstOrDefaultAsync();
        }

        public async Task<Pago> Create(Pago pago)
        {
            await _pagoCollection.InsertOneAsync(pago);
            var update = Builders<Cita>.Update.Set(cita => cita.Pagada, true);
            await _citaCollection.UpdateOneAsync(cita => cita.Id == pago.Cita.Id, update);
            return pago;
        }

        public async Task Update(Pago nuevoPago)
        {

            await _pagoCollection.ReplaceOneAsync(pago => pago.Id == nuevoPago.Id, nuevoPago);
        }

        public async Task Delete(Pago pagoborrar)
        {
            await _pagoCollection.DeleteOneAsync(pago => pago.Id == pagoborrar.Id);
        }

        public async Task DeleteById(string id)
        {
            await _pagoCollection.DeleteOneAsync(pago => pago.Id == id);
        }
        public async Task<List<Pago>> GetByPaciente(string id)
        {
            return await _pagoCollection.Find(pago => pago.Cita.Paciente.Id == id).ToListAsync();
        }
        public async Task<List<Pago>> GetByDentista(string id)
        {
            return await _pagoCollection.Find(pago => pago.Cita.Dentista.Id == id).ToListAsync();
        }
        public async Task<List<Pago>> GetByConsultorio(string id)
        {
            return await _pagoCollection.Find(pago => pago.Cita.Dentista.Consultorio == id).ToListAsync();
        }
    }
}
