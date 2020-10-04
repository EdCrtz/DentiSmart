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
            var consultorio = await _consultorioRepository.GetById(id);

            if (consultorio == null)
            {
                return NotFound();
            }

            return Ok(consultorio);
        }

        [HttpPost]
        public async Task<IActionResult> Create(Consultorio consultorio)
        {
            await _consultorioRepository.Create(consultorio);

            return CreatedAtRoute("GetConsultorio", new
            {
                id = consultorio.Id.ToString()
            }, consultorio);
        }

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
