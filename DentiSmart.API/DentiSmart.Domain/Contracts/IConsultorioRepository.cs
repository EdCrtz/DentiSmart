using DentiSmart.Domain.Models;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Domain.Contracts
{
    public interface IConsultorioRepository
    {
        public Task<List<Consultorio>> Get();
        public Task<Consultorio> GetById(string id);
        public Task<Consultorio> Create(Consultorio consultorio);
        public Task Update(Consultorio nuevoConsultorio);
        public Task Delete(Consultorio consultorioborrar);
        public Task DeleteById(string id);


    }
}
