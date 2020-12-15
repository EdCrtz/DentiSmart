using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using DentiSmart.Domain.Contracts;
using DentiSmart.Domain.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace DentiSmart.API.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]
    public class CitaController : ControllerBase
    {
        private readonly ICitaRepository _citaRepository;

        public CitaController(ICitaRepository citaRepository)
        {
            _citaRepository = citaRepository;
        }

        /// <summary>
        /// Obtener todas las citas de todos los consultorios y dentistas
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(await _citaRepository.Get());
        }

        /// <summary>
        /// Obtener una cita en especifica via id 
        /// </summary>
        /// <returns></returns>
        [HttpGet("{id:length(24)}", Name = "GetCita")]
        public async Task<IActionResult> GetById(string id)
        {
            var cita = await _citaRepository.GetById(id);

            if (cita == null)
            {
                return NotFound();
            }

            return Ok(cita);
        }
        /// <summary>
        /// Registrar un cita nueva en el sistema
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public async Task<IActionResult> Create(Cita cita)
        {
            await _citaRepository.Create(cita);

            return CreatedAtRoute("GetCita", new
            {
                id = cita.Id.ToString()
            }, cita);
        }


        /// <summary>
        /// Verificar la disponibilidad de una cita
        /// </summary>
        /// <returns></returns>
        [HttpPost("Disponible")]
        public IActionResult VerificarDisponibilidad(Cita cita)
        {
            return Ok(_citaRepository.VerificarDisponiblidad(cita));
        }
        /// <summary>
        /// Actualizar una cita existente
        /// </summary>
        /// <returns></returns>
        [HttpPut]
        public async Task<IActionResult> Update(Cita cita)
        {
            var citabuscada = await _citaRepository.GetById(cita.Id);

            if (citabuscada == null)
            {
                return NotFound();
            }

            await _citaRepository.Update(cita);

            return NoContent();
        }
        /// <summary>
        /// Cancelar una cita existente mediante el id
        /// </summary>
        /// <returns></returns>
        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> DeleteById(string id)
        {
            var cita = await _citaRepository.GetById(id);

            if (cita == null)
            {
                return NotFound();
            }

            await _citaRepository.DeleteById(cita.Id);

            return NoContent();
        }

        /// <summary>
        /// Obtener todas las citas de un consultorio en especifico medienate su id
        /// </summary>
        /// <returns></returns>
        [HttpGet("Consultorio/{consultorio}")]
        public async Task<IActionResult> GetByConsultorio(string consultorio)
        {
            return Ok(await _citaRepository.GetByConsultorio(consultorio));
        }

        /// <summary>
        /// Obtener todas las citas de un dentista en especifico mediante su id
        /// </summary>
        /// <returns></returns>
        [HttpGet("Dentista/{dentista}")]
        public async Task<IActionResult> GetByDentista(string dentista)
        {
            return Ok(await _citaRepository.GetByDentista(dentista));
        }

        /// <summary>
        /// Obtener todas las citas de un paciente en especifico mediante su id
        /// </summary>
        /// <returns></returns>
        [HttpGet("Paciente/{paciente}")]
        public async Task<IActionResult> GetByPaciente(string paciente)
        {
            return Ok(await _citaRepository.GetByPaciente(paciente));
        }
    }
}
