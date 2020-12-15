using DentiSmart.Domain.Models;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Domain.Contracts
{
    public interface IUsuarioRepository
    {
        public Task<List<Usuario>> Get();
        public Task<List<Usuario>> GetByRole(string role);
        public Task<Usuario> GetById(string id);
        public Task<Usuario> GetByUserName(string nombreUsuario);
        public Task<Usuario> Create(Usuario usuario);
        public Task Update(Usuario nuevoUsuario);
        public Task Delete(Usuario ususarioBorrar);
        public Task DeleteById(string id);
        public Task<List<Usuario>> GetDentistaByConsultorio(string consultorio);
    }
}
