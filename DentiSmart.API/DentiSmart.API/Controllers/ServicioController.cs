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
    [Authorize]
    [ApiController]
    public class ServicioController : ControllerBase
    {
        private readonly IServicioRepository _servicioRepository;

        public ServicioController(IServicioRepository servicioRepository)
        {
            _servicioRepository = servicioRepository;
        }

        /// <summary>
        /// Obtener todos los servicios existentes
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(await _servicioRepository.Get());
        }
        /// <summary>
        /// Obtener un servicio en especifico mediate tu id
        /// </summary>
        /// <returns></returns>
        [HttpGet("{id:length(24)}", Name = "GetServicio")]
        public async Task<IActionResult> GetById(string id)
        {
            var servicio = await _servicioRepository.GetById(id);

            if (servicio == null)
            {
                return NotFound();
            }

            return Ok(servicio);
        }
        /// <summary>
        /// Obtener todos los servicios existentes medianate el id de un consultorio en especifico 
        /// </summary>
        /// <returns></returns>
        [HttpGet("Consultorio/{consultorio}")]
        public async Task<IActionResult> GetByConsultorio(string consultorio)
        {
            return Ok(await _servicioRepository.GetByConsultorio(consultorio));
        }
        /// <summary>
        /// Registar un nuevo consultorio en el sistema
        /// </summary>
        /// <returns></returns>
        [Authorize(Roles = "ADMIN, Dentista")]
        [HttpPost]
        public async Task<IActionResult> Create(Servicio servicio)
        {
            await _servicioRepository.Create(servicio);

            return CreatedAtRoute("GetServicio", new
            {
                id = servicio.Id.ToString()
            }, servicio);
        }

        /// <summary>
        /// Actualizar un consultorio existente
        /// </summary>
        /// <returns></returns>
        [Authorize(Roles = "ADMIN, Dentista")]
        [HttpPut]
        public async Task<IActionResult> Update(Servicio servicio)
        {
            var servicio1 = await _servicioRepository.GetById(servicio.Id);

            if (servicio1 == null)
            {
                return NotFound();
            }

            await _servicioRepository.Update(servicio);

            return NoContent();
        }
        /// <summary>
        /// Eliminar un consultorio existente mediante el id
        /// </summary>
        /// <returns></returns>
        [Authorize(Roles = "ADMIN, Dentista")]
        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> DeleteById(string id)
        {
            var servicio = await _servicioRepository.GetById(id);

            if (servicio == null)
            {
                return NotFound();
            }

            await _servicioRepository.DeleteById(servicio.Id);

            return NoContent();
        }
    }
}
