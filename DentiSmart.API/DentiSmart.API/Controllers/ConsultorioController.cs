using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using DentiSmart.Domain.Models;
using DentiSmart.Infrastructure.Repository;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;


namespace DentiSmart.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ConsultorioController : ControllerBase
    {
        private readonly ConsultorioRepository _consultorioRepository;

        public ConsultorioController(ConsultorioRepository consultorioRepository)
        {
            _consultorioRepository = consultorioRepository;
        }

        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(await _consultorioRepository.Get());
        }

        [HttpGet("{id:length(24)}", Name = "GetConsultorio")]
        public async Task<IActionResult> GetById(string id)
        {
            var tienda = await _consultorioRepository.GetById(id);

            if (tienda == null)
            {
                return NotFound();
            }

            return Ok(tienda);
        }

        [HttpPost]
        public async Task<IActionResult> Create(Consultorio consultorio)
        {
            await _consultorioRepository.Create(consultorio);

            return CreatedAtRoute("GetTienda", new
            {
                id = consultorio.Id.ToString()
            }, consultorio);
        }

        [HttpPut("{id:length(24)}")]
        public async Task<IActionResult> Update(Consultorio consultorio)
        {
            var tienda1 = await _consultorioRepository.GetById(consultorio.Id);

            if (tienda1 == null)
            {
                return NotFound();
            }

            await _consultorioRepository.Update(consultorio);

            return NoContent();
        }

        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> DeleteById(string id)
        {
            var tienda = await _consultorioRepository.GetById(id);

            if (tienda == null)
            {
                return NotFound();
            }

            await _consultorioRepository.DeleteById(tienda.Id);

            return NoContent();
        }
    }
}
