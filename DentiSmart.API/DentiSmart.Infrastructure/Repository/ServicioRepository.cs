using DentiSmart.Domain.Models;
using DentiSmart.Infrastructure.DataBase;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using DentiSmart.Domain.Contracts;
namespace DentiSmart.Infrastructure.Repository
{
    public class ServicioRepository: IServicioRepository
    {
        private readonly IMongoCollection<Servicio> _servicioCollection;

        public ServicioRepository(IDentiSmartDatabaseSettings settings)
        {
            var mdbClient = new MongoClient(settings.ConnectionString);
            var database = mdbClient.GetDatabase(settings.DatabaseName);
            _servicioCollection = database.GetCollection<Servicio>("Servicio");
        }

        public async Task<List<Servicio>> Get()
        {
            return await _servicioCollection.Find(servicio => true).ToListAsync();
        }
        public async Task<List<Servicio>> GetByConsultorio(string consultorio)
        {
            return await _servicioCollection.Find(servicio => servicio.Consultorio == consultorio).ToListAsync();
        }

        public async Task<Servicio> GetById(string id)
        {
            return await _servicioCollection.Find(servicio => servicio.Id == id).FirstOrDefaultAsync();
        }

        public async Task<Servicio> Create(Servicio servicio)
        {
            await _servicioCollection.InsertOneAsync(servicio);
            return servicio;
        }

        public async Task Update(Servicio nuevoservicio)
        {

            await _servicioCollection.ReplaceOneAsync(servicio => servicio.Id == nuevoservicio.Id, nuevoservicio);
        }

        public async Task Delete(Servicio servicioborrar)
        {
            await _servicioCollection.DeleteOneAsync(servicio => servicio.Id == servicioborrar.Id);
        }

        public async Task DeleteById(string id)
        {
            await _servicioCollection.DeleteOneAsync(servicio => servicio.Id == id);
        }
    }
}

