using DentiSmart.Domain.Models;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Domain.Contracts
{
    public interface ITratamientoRepository
    {
        public Task<List<Tratamiento>> Get();
        public Task<Tratamiento> GetById(string id);
        public Task<Tratamiento> Create(Tratamiento tratamiento);
        public Task Update(Tratamiento nuevoTratamiento);
        public Task Delete(Tratamiento tratamientoborar);
        public Task DeleteById(string id);
        public Task<List<Tratamiento>> GetByPaciente(string id);
        public Task<List<Tratamiento>> GetByDentista(string id);
        public Task<List<Tratamiento>> GetByConsultorio(string id);
    }
}
