using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class Cita
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public Usuario Paciente { get; set; }
        public Usuario Dentista { get; set; }
        public DateTime FechaCita { get; set; }
        public DateTime FechaRegistro { get; set; } = DateTime.Now;
        public List<Servicio> Servicios { get; set; }
        public string Status { get; set; }
        public bool Pagada { get; set; }
        [BsonRepresentation(BsonType.ObjectId)]
        public string Tratamiento { get; set; }
    }
}
