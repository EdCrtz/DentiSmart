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
    [Authorize]
    public class MensajesController : ControllerBase
    {
        private readonly IMensajeRepository _mensajeRepository;

        public MensajesController(IMensajeRepository mensajeRepository)
        {
            _mensajeRepository = mensajeRepository;
        }
        /// <summary>
        /// Obtener todos los mensajes
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(await _mensajeRepository.Get());
        }
        /// <summary>
        /// Obtener todos los mensajes de un usuario
        /// </summary>
        /// <returns></returns>
        [HttpGet("{UserNameUsuarioTransmisor}")]
        public async Task<IActionResult> GetByUserName(string username)
        {
            var mensaje = await _mensajeRepository.GetByUserName(username);

            if (mensaje == null)
            {
                return NotFound();
            }

            return Ok(mensaje);
        }

        /// <summary>
        /// Crear un mensaje en el sistema
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public async Task<IActionResult> Create(Mensajes mensaje)
        {

                await _mensajeRepository.Create(mensaje);

            return Ok(mensaje);

        }


        /// <summary>
        /// Borrar un mesaje por id
        /// </summary>
        /// <returns></returns>
        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> DeleteById(string id)
        {
            var mensaje = await _mensajeRepository.GetById(id);

            if (mensaje == null)
            {
                return NotFound();
            }

            await _mensajeRepository.DeleteById(mensaje.Id);

            return NoContent();
        }
    }
}
