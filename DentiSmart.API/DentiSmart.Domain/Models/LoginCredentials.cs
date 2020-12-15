using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace DentiSmart.Domain.Models
{
   public class LoginCredentials
    {
        [Required]
        public string NombreUsuario { get; set; }
        [Required]
        public string Contrasenia { get; set; }
    }
}
