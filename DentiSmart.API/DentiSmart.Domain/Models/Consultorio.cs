using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Domain.Models
{
    public class Consultorio
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string Nombre { get; set; }
        public ICollection<Telefono> Telefonos { get; set; }

        [BsonElement("Direccion")]
        public Direccion DireccioConsultorio { get; set; }
    }
}
