using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using DentiSmart.Domain.Contracts;
using DentiSmart.Domain.Models;
using DentiSmart.Infrastructure.Repository;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace DentiSmart.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase
    {
        private readonly ILoginRepository _loginRepository;

        public LoginController(ILoginRepository loginRepository)
        {
            _loginRepository = loginRepository;
        }
        /// <summary>
        /// Autenticación para usuarios del sistema por medio de usuario y contraseña
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public async Task<IActionResult> Login([FromBody] LoginCredentials user)
        { 
            var result = await _loginRepository.Login(user);
            if (result == null)
            {
                return NotFound();
            }
            return Ok(result);
        }

        /// <summary>
        /// Autenticación para usuarios del sistema por medio una credencial de facebook
        /// </summary>
        /// <returns></returns>
        [HttpPost("Facebook")]
        public async Task<IActionResult> LoginFacebook([FromBody] Usuario user)
        {
            var result = await _loginRepository.LoginFacebook(user);
            if (result == null)
            {
                return NotFound();
            }
            return Ok(result);
        }

        /// <summary>
        /// Autenticación para usuarios del sistema por medio una credencial de google
        /// </summary>
        /// <returns></returns>
        [HttpPost("Google")]
        public async Task<IActionResult> LoginGoogle([FromBody] Usuario user)
        {
            var result = await _loginRepository.LoginGoogle(user);
            if (result == null)
            {
                return NotFound();
            }
            return Ok(result);
        }
    }

}
