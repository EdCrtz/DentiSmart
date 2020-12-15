using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Domain.Models
{
    public class ServiceMessage
    {
        public int Status { get; set; }
        public string Message { get; set; }
        public Object Data { get; set; }
    }
}
