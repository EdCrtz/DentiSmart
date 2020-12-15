using DentiSmart.Domain.Models;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Domain.Contracts
{
    public interface IInventarioRepository
    {
        public Task<List<Inventario>> Get();
        public Task<Inventario> GetById(string id);
        public Task<Inventario> Create(Inventario inventario);
        public Task Update(Inventario nuevoinventario);
        public Task Delete(Inventario inventarioborrar);

        public Task<List<Inventario>> GetByConsultorio(string id);

        public Task DeleteById(string id);
    }
}
