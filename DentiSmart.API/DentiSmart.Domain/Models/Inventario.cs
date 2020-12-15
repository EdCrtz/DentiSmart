using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class Inventario
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        [Required]
        public ICollection<ProductosInventario> ProductosInventario { get; set; }
        [Required]
        public DateTime FechaModificacion { get; set; } = DateTime.Now;
        [BsonRepresentation(BsonType.ObjectId)]
        [Required]
        public string Consultorio { get; set; }
        [Required]
        public Usuario Usuario { get; set; }
       
      
    }
}
