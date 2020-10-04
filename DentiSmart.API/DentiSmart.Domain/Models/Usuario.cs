using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Domain.Models
{
    public class Usuario
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string Nombre { get; set; }
        public string ApellidoPat { get; set; }
        public string ApellidoMat { get; set; }
        public string NombreUsuario { get; set; }
        public string Contrasenia { get; set;}
        [BsonElement("Telefono")]
        public Telefono Telefono { get; set; }
    }
}
