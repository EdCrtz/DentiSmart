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
    public class TratamientoRepository : ITratamientoRepository
    {

        private readonly IMongoCollection<Tratamiento> _tratamientoCollection;
        private readonly IMongoCollection<Cita> _citaCollection;
        public TratamientoRepository(IDentiSmartDatabaseSettings settings)
        {
            var mdbClient = new MongoClient(settings.ConnectionString);
            var database = mdbClient.GetDatabase(settings.DatabaseName);
            _tratamientoCollection = database.GetCollection<Tratamiento>("Tratamiento");
            _citaCollection = database.GetCollection<Cita>("Cita");
        }
        public async Task<Tratamiento> Create(Tratamiento tratamiento)
        {
            await _tratamientoCollection.InsertOneAsync(tratamiento);
            var update = Builders<Cita>.Update.Set(cita => cita.Tratamiento, tratamiento.Id);
            await _citaCollection.UpdateOneAsync(cita => cita.Id == tratamiento.Cita.Id, update);
            return tratamiento;
        }

        public async Task Delete(Tratamiento tratamientoborar)
        {
            await _tratamientoCollection.DeleteOneAsync(tratamiento => tratamiento.Id == tratamientoborar.Id);
        }

        public async Task DeleteById(string id)
        {
            await _tratamientoCollection.DeleteOneAsync(tratamiento => tratamiento.Id == id);
        }

        public async Task<List<Tratamiento>> Get()
        {
            return await _tratamientoCollection.Find(tratamiento => true).ToListAsync();
        }

        public async Task<List<Tratamiento>> GetByConsultorio(string id)
        {
            return await _tratamientoCollection.Find(tratamiento => tratamiento.Cita.Dentista.Consultorio == id).ToListAsync();
        }

        public async Task<List<Tratamiento>> GetByDentista(string id)
        {
            return await _tratamientoCollection.Find(tratamiento => tratamiento.Cita.Dentista.Id == id).ToListAsync();
        }

        public async Task<Tratamiento> GetById(string id)
        {
            return await _tratamientoCollection.Find(tratamiento => tratamiento.Id == id).FirstOrDefaultAsync();
        }

        public async Task<List<Tratamiento>> GetByPaciente(string id)
        {
            return await _tratamientoCollection.Find(tratamiento => tratamiento.Cita.Paciente.Id == id).ToListAsync();
        }

        public async Task Update(Tratamiento nuevoTratamiento)
        {
            await _tratamientoCollection.ReplaceOneAsync(tratamiento => tratamiento.Id == nuevoTratamiento.Id, nuevoTratamiento);
        }
    }
}
