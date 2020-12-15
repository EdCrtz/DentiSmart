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
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]

    public class ConsultorioController : ControllerBase
    {
        private readonly IConsultorioRepository _consultorioRepository;

        public ConsultorioController(IConsultorioRepository consultorioRepository)
        {
            _consultorioRepository = consultorioRepository;
        }
        /// <summary>
        /// Obtener todos los consultorios existentes
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(await _consultorioRepository.Get());
        }

        /// <summary>
        /// Obtener un consultorio existente por id
        /// </summary>
        /// <returns></returns>
        [HttpGet("{id:length(24)}", Name = "GetConsultorio")]
        public async Task<IActionResult> GetById(string id)
        {
            var consultorio = await _consultorioRepository.GetById(id);

            if (consultorio == null)
            {
                return NotFound();
            }

            return Ok(consultorio);
        }
        /// <summary>
        /// Registrar un nuevo consultorio en el sistema
        /// </summary>
        /// <returns></returns>
        [Authorize(Roles = "ADMIN")]
        [HttpPost]
        public async Task<IActionResult> Create(Consultorio consultorio)
        {
            await _consultorioRepository.Create(consultorio);

            return CreatedAtRoute("GetConsultorio", new
            {
                id = consultorio.Id.ToString()
            }, consultorio);
        }
        /// <summary>
        /// Actualizar un consultorio existente
        /// </summary>
        /// <returns></returns>
        [Authorize(Roles = "ADMIN")]
        [HttpPut]
        public async Task<IActionResult> Update(Consultorio consultorio)
        {
            var consultorio1 = await _consultorioRepository.GetById(consultorio.Id);

            if (consultorio1 == null)
            {
                return NotFound();
            }

            await _consultorioRepository.Update(consultorio);

            return NoContent();
        }
        /// <summary>
        /// Eliminar un consultorio existente por id
        /// </summary>
        /// <returns></returns>
        [Authorize(Roles = "ADMIN")]
        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> DeleteById(string id)
        {
            var consultorio = await _consultorioRepository.GetById(id);

            if (consultorio == null)
            {
                return NotFound();
            }

            await _consultorioRepository.DeleteById(consultorio.Id);

            return NoContent();
        }

    }
}
