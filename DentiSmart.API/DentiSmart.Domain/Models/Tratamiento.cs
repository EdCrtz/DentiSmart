using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class Tratamiento
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public DateTime FechaCreacion { get; set; } = DateTime.Now;
        [Required]
        public Cita Cita { get; set; }
        public Odontograma Odontograma { get; set; }
        public List<DetalleTratamiento> Detalle {get; set;}
    }
}
