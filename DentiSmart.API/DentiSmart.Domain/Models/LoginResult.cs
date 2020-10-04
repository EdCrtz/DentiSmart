using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Domain.Models
{
    public class LoginResult: Usuario
    {
        public string Token { get; set; }
        public string RefreshToken { get; set; }
    }
}
