using DentiSmart.Domain.Models;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Domain.Contracts
{
    public interface IServicioRepository
    {
        public Task<List<Servicio>> Get();
        public Task<List<Servicio>> GetByConsultorio(string consultorio);
        public Task<Servicio> GetById(string id);
        public Task<Servicio> Create(Servicio servicio);
        public Task Update(Servicio nuevoservicio);
        public Task Delete(Servicio servioborrar);
        public Task DeleteById(string id);
    }
}
