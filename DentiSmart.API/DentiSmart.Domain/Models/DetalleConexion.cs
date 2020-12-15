using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class DetalleConexion
    {
        public string ConnectionId { get; set; }
        public string UsuarioID { get; set; }
        public string NombreUsuario { get; set; }

    }
}
