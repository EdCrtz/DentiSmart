using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class Consultorio
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        [Required]
        public string Nombre { get; set; }
        [Required]
        public ICollection<Telefono> Telefonos { get; set; }

        [BsonElement("Direccion")]
        [Required]
        public Direccion DireccionConsultorio { get; set; }
    }
}
