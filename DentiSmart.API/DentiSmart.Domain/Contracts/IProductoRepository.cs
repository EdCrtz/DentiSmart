using DentiSmart.Domain.Models;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Domain.Contracts
{
    public interface IProductoRepository
    {
        public Task<List<Producto>> Get();
        public Task<List<Producto>> GetByConsultorio(string consultorio);
        public Task<Producto> GetById(string id);
        public Task<Producto> Create(Producto producto);
        public Task Update(Producto nuevoproducto);
        public Task Delete(Producto productoborar);
        public Task DeleteById(string id);
    }
}
