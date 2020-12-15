using System;
using System.Collections.Generic;
using System.Text;
using DentiSmart.Domain.Models;
using System.Threading.Tasks;

namespace DentiSmart.Domain.Contracts
{
    public interface IMensajeRepository
    {
        public Task<List<Mensajes>> Get();
        public Task<List<Mensajes>> GetByUserName(string nombreUsuario);
        public Task<Mensajes> GetById(string id);

        public Task<Mensajes> Create(Mensajes mensaje);
      
        public Task DeleteById(string id);

    }
}
