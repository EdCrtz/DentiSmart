using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using DentiSmart.Domain.Contracts;
using DentiSmart.Domain.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
namespace DentiSmart.API.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]
    public class TratamientoController : ControllerBase
    {
        private readonly ITratamientoRepository _tratamientoRepository;

        public TratamientoController(ITratamientoRepository tratamientoRepository)
        {
            _tratamientoRepository = tratamientoRepository;
        }
        /// <summary>
        /// Obtener todos los tratamientos existentes
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(await _tratamientoRepository.Get());
        }
        /// <summary>
        /// Obtener un tratamiento en especifico por id 
        /// </summary>
        /// <returns></returns>
        [HttpGet("{id:length(24)}", Name = "GetTratamiento")]
        public async Task<IActionResult> GetById(string id)
        {
            var Tratamiento = await _tratamientoRepository.GetById(id);

            if (Tratamiento == null)
            {
                return NotFound();
            }

            return Ok(Tratamiento);
        }
        /// <summary>
        /// Registrar un nuevo tratamiento en el sistema
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public async Task<IActionResult> Create(Tratamiento Tratamiento)
        {
            await _tratamientoRepository.Create(Tratamiento);

            return CreatedAtRoute("GetTratamiento", new
            {
                id = Tratamiento.Id.ToString()
            }, Tratamiento);
        }
        /// <summary>
        /// Actualizar un tratamiento del sistema
        /// </summary>
        /// <returns></returns>
        [HttpPut]
        public async Task<IActionResult> Update(Tratamiento Tratamiento)
        {
            var Tratamiento1 = await _tratamientoRepository.GetById(Tratamiento.Id);

            if (Tratamiento1 == null)
            {
                return NotFound();
            }

            await _tratamientoRepository.Update(Tratamiento);

            return NoContent();
        }
        /// <summary>
        /// Eliminar un tratamiento del sistema por id
        /// </summary>
        /// <returns></returns>
        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> DeleteById(string id)
        {
            var Tratamiento = await _tratamientoRepository.GetById(id);

            if (Tratamiento == null)
            {
                return NotFound();
            }

            await _tratamientoRepository.DeleteById(Tratamiento.Id);

            return NoContent();
        }
        /// <summary>
        /// Obtener todos los tratamientos existentes de un consultorio en especifico mediante el id
        /// </summary>
        /// <returns></returns>
        [HttpGet("Consultorio/{consultorio}")]
        public async Task<IActionResult> GetByConsultorio(string consultorio)
        {
            return Ok(await _tratamientoRepository.GetByConsultorio(consultorio));
        }

        /// <summary>
        /// Obtener todos los tratamientos existentes de un dentista en especifico mediante el id
        /// </summary>
        /// <returns></returns>
        [HttpGet("Dentista/{dentista}")]
        public async Task<IActionResult> GetByDentista(string dentista)
        {
            return Ok(await _tratamientoRepository.GetByDentista(dentista));
        }
        /// <summary>
        /// Obtener todos los tratamientos existentes de un paciente en especifico mediante el id
        /// </summary>
        /// <returns></returns>
        [HttpGet("Paciente/{paciente}")]
        public async Task<IActionResult> GetByPaciente(string paciente)
        {
            return Ok(await _tratamientoRepository.GetByPaciente(paciente));
        }
    }
}
