using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class Direccion
    {
        [Required]
        public string Calle { get; set; }
        [Required]
        public string Numero { get; set; }
        public string Depto { get; set; }
        [Required]
        public string Colonia { get; set; }
    }
}
