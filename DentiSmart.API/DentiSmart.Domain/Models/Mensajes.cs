using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class Mensajes
    {
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string IDUsuarioTransmisor { get; set; }
        public string UserNameUsuarioTransmisor { get; set; }
        public string IDUsuarioReceptor { get; set; }
        public string UserNameUsuarioReceptor { get; set; }
        public string Mensaje { get; set; }
        public string Fecha { get; set; }


    }
}
