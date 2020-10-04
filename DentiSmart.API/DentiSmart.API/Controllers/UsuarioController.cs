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
    public class UsuarioController : ControllerBase
    {
        private readonly UsuarioRepository _usuarioRepository;

        public UsuarioController(UsuarioRepository usuarioRepository)
        {
            _usuarioRepository = usuarioRepository;
        }

        [HttpGet]
        public async Task<IActionResult> Get()
        {
            return Ok(await _usuarioRepository.Get());
        }

        [HttpGet("{id:length(24)}", Name = "GetUsuario")]
        public async Task<IActionResult> GetById(string id)
        {
            var usuario = await _usuarioRepository.GetById(id);

            if (usuario == null)
            {
                return NotFound();
            }

            return Ok(usuario);
        }

        [HttpPost]
        public async Task<IActionResult> Create(Usuario usuario)
        {
            await _usuarioRepository.Create(usuario);

            return CreatedAtRoute("GetUsuario", new
            {
                id = usuario.Id.ToString()
            }, usuario);
        }

        [HttpPut]
        public async Task<IActionResult> Update(Usuario usuario)
        {
            var usuario1 = await _usuarioRepository.GetById(usuario.Id);

            if (usuario1 == null)
            {
                return NotFound();
            }

            await _usuarioRepository.Update(usuario);

            return NoContent();
        }

        [HttpDelete("{id:length(24)}")]
        public async Task<IActionResult> DeleteById(string id)
        {
            var usuario = await _usuarioRepository.GetById(id);

            if (usuario == null)
            {
                return NotFound();
            }

            await _usuarioRepository.DeleteById(usuario.Id);

            return NoContent();
        }
    }
}
