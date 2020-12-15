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
    [Authorize(Roles = "ADMIN, Dentista")]
    [ApiController]
    public class InventarioController : ControllerBase
    {
        private readonly IInventarioRepository _inventarioRepository;

        public InventarioController(IInventarioRepository inventarioRepository)
        {
            _inventarioRepository = inventarioRepository;
        }
        /// <summary>
        /// Obtener todos los registros del inventario
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(await _inventarioRepository.Get());
        }
        /// <summary>
        /// Obtener un registro en especifico del inventario mediante id
        /// </summary>
        /// <returns></returns>
        [HttpGet("{id:length(24)}", Name = "GetInventario")]
        public async Task<IActionResult> GetById(string id)
        {
            var inventario = await _inventarioRepository.GetById(id);

            if (inventario == null)
            {
                return NotFound();
            }

            return Ok(inventario);
        }
        /// <summary>
        /// Registar datos al registro de inventario
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public async Task<IActionResult> Create(Inventario inventario)
        {
            await _inventarioRepository.Create(inventario);

            return CreatedAtRoute("GetInventario", new
            {
                id = inventario.Id.ToString()
            }, inventario);
        }
        /// <summary>
        /// Actualizar un registro del inventario en especifico
        /// </summary>
        /// <returns></returns>
        [HttpPut]
        public async Task<IActionResult> Update(Inventario inventario)
        {
            var invenario1 = await _inventarioRepository.GetById(inventario.Id);

            if (invenario1 == null)
            {
                return NotFound();
            }

            await _inventarioRepository.Update(inventario);

            return NoContent();
        }
        /// <summary>
        /// Eliminar un registro del inventario en especifico
        /// </summary>
        /// <returns></returns>
        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> DeleteById(string id)
        {
            var inventario = await _inventarioRepository.GetById(id);

            if (inventario == null)
            {
                return NotFound();
            }

            await _inventarioRepository.DeleteById(inventario.Id);

            return NoContent();
        }


        [HttpGet("Consultorio/{id}")]
        public async Task<IActionResult> GetByConsultorio(string id)
        {
            var inventario = await _inventarioRepository.GetByConsultorio(id);

            if (inventario == null)
            {
                return NotFound();
            }

            return Ok(inventario);
        }
    }
}
