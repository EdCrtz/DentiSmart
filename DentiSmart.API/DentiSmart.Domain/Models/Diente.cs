using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class Diente
    {
        public int Izquierdo { get; set; }
        public int Derecho { get; set; }
        public int Centro { get; set; }
        public int Norte { get; set; }
        public int Sur { get; set; }
        public int Estado { get; set; }
    }
}
