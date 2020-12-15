using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Domain.Models
{
    public class ProductosInventario
    {
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string Nombre { get; set; }
        public decimal CantidadAnterior { get; set; }
        public decimal CantidadActualizada { get; set; }
        public decimal precio { get; set; }

    }
}
