using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class Telefono
    {
        public int Id { get; set; }
        public string Tel { get; set; }
    }
}
