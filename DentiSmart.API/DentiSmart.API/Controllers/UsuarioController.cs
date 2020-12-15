using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using DentiSmart.Domain.Contracts;
using DentiSmart.Domain.Models;
using DentiSmart.Infrastructure.Repository;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace DentiSmart.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsuarioController : ControllerBase
    {
        private readonly IUsuarioRepository _usuarioRepository;

        public UsuarioController(IUsuarioRepository usuarioRepository)
        {
            _usuarioRepository = usuarioRepository;
        }
        /// <summary>
        /// Obtener todos los usuarios existentes
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [Authorize(Roles = "ADMIN, Dentista")]
        public async Task<IActionResult> Get()
        {
            return Ok(await _usuarioRepository.Get());
        }
        /// <summary>
        /// Obtener un usuario en especifico mediante el id
        /// </summary>
        /// <returns></returns>
        [HttpGet("{id:length(24)}", Name = "GetUsuario")]
        [Authorize]
        public async Task<IActionResult> GetById(string id)
        {
            var usuario = await _usuarioRepository.GetById(id);

            if (usuario == null)
            {
                return NotFound();
            }

            return Ok(usuario);
        }
        /// <summary>
        /// Obtener todos los usuarios de un role en especifico
        /// </summary>
        /// <returns></returns>
        [HttpGet("Role/{role}")]
        [Authorize]
        public async Task<IActionResult> GetByRole(string role)
        {
            return Ok(await _usuarioRepository.GetByRole(role));
        }
        /// <summary>
        /// Obtener todos los usuarios de role dentista de un consultorio en espesifico
        /// </summary>
        /// <returns></returns>
        [HttpGet("Dentista/{consultorio}")]
        [Authorize]
        public async Task<IActionResult> GetDentistaByConsultorio(string consultorio)
        {
            return Ok(await _usuarioRepository.GetDentistaByConsultorio(consultorio));
        }
        /// <summary>
        /// Registrar un usuario en el sistema
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public async Task<IActionResult> Create(Usuario usuario)
        {

            var busqueda = await _usuarioRepository.GetByUserName(usuario.NombreUsuario);

            if (busqueda == null)
            {
                await _usuarioRepository.Create(usuario);

                return CreatedAtRoute("GetUsuario", new
                {
                    id = usuario.Id.ToString()
                }, usuario);
            }
            return BadRequest();
           
        }
        /// <summary>
        /// Actualizar un usuario existente
        /// </summary>
        /// <returns></returns>
        [HttpPut]
        [Authorize]
        public async Task<IActionResult> Update(Usuario usuario)
        {
            var usuarioNoModificado = await _usuarioRepository.GetById(usuario.Id);

            if (usuarioNoModificado == null)
            {
                return NotFound();
            }
            //Validar para que no se vuelva enciptar la contraseña
            if (!usuario.Contrasenia.Equals(usuarioNoModificado.Contrasenia))
            {
                usuario.Contrasenia = BCrypt.Net.BCrypt.HashPassword(usuario.Contrasenia);
            }
            // Validar que no se repita el nombre de usuario
            if (!usuario.NombreUsuario.Equals(usuarioNoModificado.NombreUsuario))
            {
                var busqueda = await _usuarioRepository.GetByUserName(usuario.NombreUsuario);
                if (busqueda != null)
                    return BadRequest();
            }
            await _usuarioRepository.Update(usuario);
            return NoContent();
        }
        /// <summary>
        /// Eliminar un usuario existente mediante el id
        /// </summary>
        /// <returns></returns>
        [Authorize(Roles = "ADMIN, Dentista")]
        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> DeleteById(string id)
        {
            var usuario = await _usuarioRepository.GetById(id);

            if (usuario == null)
            {
                return NotFound();
            }

            await _usuarioRepository.DeleteById(usuario.Id);

            return NoContent();
        }
    }
}
