using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Domain.Models
{
    public interface IPagoRepository
    {
        public Task<List<Pago>> Get();
        public Task<Pago> GetById(string id);
        public Task<Pago> Create(Pago pago);
        public Task Update(Pago nuevopago);
        public Task Delete(Pago pagoborar);
        public Task DeleteById(string id);
        public Task<List<Pago>> GetByPaciente(string id);
        public Task<List<Pago>> GetByDentista(string id);
        public Task<List<Pago>> GetByConsultorio(string id);
    }

}
