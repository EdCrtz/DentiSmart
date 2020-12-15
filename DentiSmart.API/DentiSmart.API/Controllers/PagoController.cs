using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using DentiSmart.Domain.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace DentiSmart.API.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]
    public class PagoController : ControllerBase
    {
        private readonly IPagoRepository _pagoRepository;

        public PagoController(IPagoRepository pagoRepository)
        {
            _pagoRepository = pagoRepository;
        }
        /// <summary>
        /// Obtener todos los pagos existentes
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(await _pagoRepository.Get());
        }
        /// <summary>
        /// Obtener un pago en especifico por id 
        /// </summary>
        /// <returns></returns>
        [HttpGet("{id:length(24)}", Name = "GetPago")]
        public async Task<IActionResult> GetById(string id)
        {
            var pago = await _pagoRepository.GetById(id);

            if (pago == null)
            {
                return NotFound();
            }

            return Ok(pago);
        }
        /// <summary>
        /// Registrar un nuevo pago en el sistema
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public async Task<IActionResult> Create(Pago pago)
        {
            await _pagoRepository.Create(pago);

            return CreatedAtRoute("GetPago", new
            {
                id = pago.Id.ToString()
            }, pago);
        }
        /// <summary>
        /// Actualizar un pago del sistema
        /// </summary>
        /// <returns></returns>
        [HttpPut]
        public async Task<IActionResult> Update(Pago pago)
        {
            var pago1 = await _pagoRepository.GetById(pago.Id);

            if (pago1 == null)
            {
                return NotFound();
            }

            await _pagoRepository.Update(pago);

            return NoContent();
        }
        /// <summary>
        /// Eliminar un pago del sistema por id
        /// </summary>
        /// <returns></returns>
        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> DeleteById(string id)
        {
            var pago = await _pagoRepository.GetById(id);

            if (pago == null)
            {
                return NotFound();
            }

            await _pagoRepository.DeleteById(pago.Id);

            return NoContent();
        }
        /// <summary>
        /// Obtener todos los pagos existentes de un consultorio en especifico mediante el id
        /// </summary>
        /// <returns></returns>
        [HttpGet("Consultorio/{consultorio}")]
        public async Task<IActionResult> GetByConsultorio(string consultorio)
        {
            return Ok(await _pagoRepository.GetByConsultorio(consultorio));
        }

        /// <summary>
        /// Obtener todos los pagos existentes de un dentista en especifico mediante el id
        /// </summary>
        /// <returns></returns>
        [HttpGet("Dentista/{dentista}")]
        public async Task<IActionResult> GetByDentista(string dentista)
        {
            return Ok(await _pagoRepository.GetByDentista(dentista));
        }
        /// <summary>
        /// Obtener todos los pagos existentes de un paciente en especifico mediante el id
        /// </summary>
        /// <returns></returns>
        [HttpGet("Paciente/{paciente}")]
        public async Task<IActionResult> GetByPaciente(string paciente)
        {
            return Ok(await _pagoRepository.GetByPaciente(paciente));
        }
    }
}
