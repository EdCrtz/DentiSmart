using DentiSmart.Domain.Models;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace DentiSmart.Domain.Contracts
{
    public interface ILoginRepository
    {
        public Task<LoginResult> Login(LoginCredentials credentials);
        public Task<LoginResult> LoginFacebook(Usuario credentials);
        public Task<LoginResult> LoginGoogle(Usuario credentials);
    }
}
