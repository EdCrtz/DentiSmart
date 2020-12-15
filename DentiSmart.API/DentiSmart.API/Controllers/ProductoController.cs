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
    [Authorize(Roles ="ADMIN, Dentista")]
    [ApiController]
    public class ProductoController : ControllerBase
    {
        private readonly IProductoRepository _productoRepository;

        public ProductoController(IProductoRepository productoRepository)
        {
            _productoRepository = productoRepository;
        }
        /// <summary>
        /// Obtener todos los productos existentes
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(await _productoRepository.Get());
        }
        /// <summary>
        /// Obtener un producto existente mediante el id
        /// </summary>
        /// <returns></returns>
        [HttpGet("{id:length(24)}", Name = "GetProducto")]
        public async Task<IActionResult> GetById(string id)
        {
            var producto = await _productoRepository.GetById(id);

            if (producto == null)
            {
                return NotFound();
            }

            return Ok(producto);
        }
        /// <summary>
        /// Obtener todos los productos existentes de un consultorio en especifico mediante el id
        /// </summary>
        /// <returns></returns>
        [HttpGet("Consultorio/{consultorio}")]
        public async Task<IActionResult> GetByConsultorio(string consultorio)
        {
            return Ok(await _productoRepository.GetByConsultorio(consultorio));
        }
        /// <summary>
        /// Registrar un nuevo producto en el sistema
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public async Task<IActionResult> Create(Producto producto)
        {
            await _productoRepository.Create(producto);

            return CreatedAtRoute("GetProducto", new
            {
                id = producto.Id.ToString()
            }, producto);
        }
        /// <summary>
        /// Actualizar un producto existente 
        /// </summary>
        /// <returns></returns>
        [HttpPut]
        public async Task<IActionResult> Update(Producto producto)
        {
            var producto1 = await _productoRepository.GetById(producto.Id);

            if (producto1 == null)
            {
                return NotFound();
            }

            await _productoRepository.Update(producto);

            return NoContent();
        }
        /// <summary>
        /// Eliminar un producto existente mediante su id 
        /// </summary>
        /// <returns></returns>
        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> DeleteById(string id)
        {
            var producto = await _productoRepository.GetById(id);

            if (producto == null)
            {
                return NotFound();
            }

            await _productoRepository.DeleteById(producto.Id);

            return NoContent();
        }
    }
}
