using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class Odontograma
    {
        public List<Diente> DientesSuperior { get; set; }
        public List<Diente> DientesInferior { get; set; }
    }
}
