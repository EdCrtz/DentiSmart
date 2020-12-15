using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class Pago
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        [Required]
        public DateTime FechaPago { get; set; } = DateTime.Now;
        [Required]
        public decimal Monto { get; set; }
        [BsonRepresentation(BsonType.ObjectId)]
        [Required]
        public string IdUsuario { get; set; }
        [Required]
        public Cita Cita { get; set; }
        [Required]
        public int Metodo { get; set; }
    }
}
