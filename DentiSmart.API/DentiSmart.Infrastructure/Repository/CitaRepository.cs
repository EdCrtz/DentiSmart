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
    public class CitaRepository : ICitaRepository
    {
        private readonly IMongoCollection<Cita> _citaCollection;

        public CitaRepository(IDentiSmartDatabaseSettings settings)
        {
            var mdbClient = new MongoClient(settings.ConnectionString);
            var database = mdbClient.GetDatabase(settings.DatabaseName);
            _citaCollection = database.GetCollection<Cita>("Cita");
        }

        public async Task<List<Cita>> Get()
        {
            return await _citaCollection.Find(cita => true).ToListAsync();
        }

        public async Task<Cita> GetById(string id)
        {
            return await _citaCollection.Find(cita => cita.Id == id).FirstOrDefaultAsync();
        }

        public async Task<Cita> Create(Cita cita)
        {
            await _citaCollection.InsertOneAsync(cita);
            return cita;
        }

        public async Task Update(Cita nuevaCita)
        {

            await _citaCollection.ReplaceOneAsync(cita => cita.Id == nuevaCita.Id, nuevaCita);
        }

        public async Task Delete(Consultorio citaBorrar)
        {
            var update = Builders<Cita>.Update.Set(cita => cita.Status, "Cancelada");
            await _citaCollection.UpdateOneAsync(cita => cita.Id == citaBorrar.Id, update);
        }

        public async Task DeleteById(string id)
        {
            var update = Builders<Cita>.Update.Set(cita => cita.Status, "Cancelada");
            await _citaCollection.UpdateOneAsync(cita => cita.Id == id, update);
        }

        public async Task<List<Cita>> GetByPaciente(string id)
        {
            return await _citaCollection.Find(cita => cita.Paciente.Id == id && cita.Status != "Cancelada").ToListAsync();
        }
        public async Task<List<Cita>> GetByDentista(string id)
        {
            return await _citaCollection.Find(cita => cita.Dentista.Id == id).ToListAsync();
        }
        public async Task<List<Cita>> GetByConsultorio(string id)
        {
            return await _citaCollection.Find(cita => cita.Dentista.Consultorio == id).ToListAsync();
        }
        public ServiceMessage VerificarDisponiblidad(Cita candidata) {
            var encontrar = _citaCollection.Find(cita => cita.Dentista.Id == candidata.Dentista.Id &&cita.FechaCita == candidata.FechaCita).FirstOrDefault();
            ServiceMessage message = new ServiceMessage();
            if (encontrar == null)
            {
                message.Status = 1;
                message.Message = "Cita disponible";
            }
            else if (candidata.Id == encontrar.Id)
            {
                message.Status = 1;
                message.Message = "Cita disponible";
            }
            else {
                message.Status = 0;
                message.Message = "Cita no disponible";
            }
            return message;

        }
        public List<Cita> GetCitasDiaSiguiente()
        {
            DateTime hoy = DateTime.Now.Date;
            DateTime manana = hoy.AddDays(1);
            DateTime pasado = hoy.AddDays(2);
            return _citaCollection.Find(x => x.FechaCita > manana && x.FechaCita < pasado).ToList();
        }
    }
}
