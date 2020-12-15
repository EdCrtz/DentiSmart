using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace DentiSmart.Domain.Models
{
    public class AntecedentesPatologicos
    {

        public string nombre { get; set; }
        public bool estatus { get; set; }
        public int? año { get; set; }




    }
}


