using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace DentiSmart.Domain.Models
{
    public class Expediente
    {

        public DateTime CreacionExpediente { get; set; } = DateTime.Now;
        public List<AntecedentesPatologicos> Antecedentes { get; set; }
        public string? DescripcionOtro { get; set; }
        public List<string>? Alergias { get; set; }
    }
}
