using DentiSmart.Domain.Models;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Domain.Contracts
{
    public interface ICitaRepository
    {
        public Task<List<Cita>> Get();
        public Task<Cita> GetById(string id);
        public Task<Cita> Create(Cita cita);
        public Task Update(Cita nuevaCita);
        public Task Delete(Consultorio citaBorrar);
        public Task DeleteById(string id);
        public List<Cita> GetCitasDiaSiguiente();
        public Task<List<Cita>> GetByPaciente(string id);
        public Task<List<Cita>> GetByDentista(string id);
        public Task<List<Cita>> GetByConsultorio(string id);
        public ServiceMessage VerificarDisponiblidad(Cita candidata);
    }
}
