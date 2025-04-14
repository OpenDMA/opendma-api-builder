use opendma_api::OdmaQName;
use opendma_api::OdmaId;
use opendma_api::OdmaGuid;
use opendma_api::OdmaType;
use opendma_api::OdmaProperty;
use opendma_api::OdmaPropertyImpl;
use std::boxed::Box;
use chrono::Utc;

fn main() {

    println!("Welcome to OpenDMA API!");

    println!("----OdmaQName----");

    //let qn = OdmaQName::new(None, "world");
    //let qn = OdmaQName::new("hello", None);
    //let qn = OdmaQName::new("", "world");
    //let qn = OdmaQName::new("hello", "");
    let qn = OdmaQName::new("hello", "world");
    println!("{}", qn);
    let qn2 = OdmaQName::new("Hello", "world");
    let qn3 = OdmaQName::new("hello", "World");
    println!("{}", qn==qn2);
    println!("{}", qn==qn3);
    let qn4 = OdmaQName::new("hello", "world");
    println!("{}", qn==qn4);

    println!("----OdmaId----");

    //let id = OdmaId::new(None);
    //let id = OdmaId::new("");
    let id = OdmaId::new("hello");
    println!("{}", id);
    let id2 = OdmaId::new("Hello");
    let id3 = OdmaId::new("hello");
    println!("{}", id==id2);
    println!("{}", id==id3);

    println!("----OdmaGuid----");

    //let guid = OdmaGuid::new(None, id.clone());
    //let guid = OdmaGuid::new(id.clone(), None);
    let guid = OdmaGuid::new(id.clone(), id.clone());
    println!("{}", guid);
    let guid2 = OdmaGuid::new(OdmaId::new("hello"), OdmaId::new("hello"));
    let guid3 = OdmaGuid::new(OdmaId::new("Hello"), OdmaId::new("hello"));
    let guid4 = OdmaGuid::new(OdmaId::new("hello"), OdmaId::new("Hello"));
    println!("{}", guid==guid2);
    println!("{}", guid==guid3);
    println!("{}", guid==guid4);

    println!("----OdmaPropertyImpl----");

    let mut pi_string = OdmaPropertyImpl::new(qn.clone(), Some(Box::new("hello".to_string())), OdmaType::STRING, false, false).unwrap();
    let pi_integer = OdmaPropertyImpl::new(qn.clone(), Some(Box::new(123i32)), OdmaType::INTEGER, false, false).unwrap();
    let pi_double = OdmaPropertyImpl::new(qn.clone(), Some(Box::new(1.23f64)), OdmaType::DOUBLE, false, false).unwrap();
    let pi_date = OdmaPropertyImpl::new(qn.clone(), Some(Box::new(Utc::now())), OdmaType::DATETIME, false, false).unwrap();

    print_any_value::<String>(pi_string.get_value());
    println!("{}", pi_string.is_multi_value());
    println!("{}", pi_string.is_read_only());
    println!("{}", pi_string.is_dirty());
    println!("{:?}", pi_string.get_string().unwrap());
    println!("{:?}", pi_string.get_integer());

    print_any_value::<i32>(pi_integer.get_value());
    println!("{:?}", pi_integer.get_integer().unwrap());
    println!("{:?}", pi_integer.get_string());

    print_any_value::<f64>(pi_double.get_value());
    println!("{:?}", pi_double.get_double().unwrap());
    print_any_value::<chrono::DateTime<Utc>>(pi_date.get_value());
    println!("{:?}", pi_date.get_datetime().unwrap());

    println!("{}", pi_string.is_dirty());
    pi_string.set_value(Some(Box::new("world".to_string()))).unwrap();
    println!("{}", pi_string.is_dirty());
    println!("{:?}", pi_string.get_string().unwrap());

    pi_string.set_value(None).unwrap();
    println!("{:?}", pi_string.get_string().unwrap());

    let mut pi_string_ro = OdmaPropertyImpl::new(qn.clone(), Some(Box::new("hello".to_string())), OdmaType::STRING, false, true).unwrap();

    println!("{:?}", pi_string_ro.get_string().unwrap());
    println!("{}", pi_string_ro.is_read_only());

    println!("{:?}", pi_string_ro.set_value(Some(Box::new("world".to_string()))) );
    println!("{:?}", OdmaPropertyImpl::new(qn.clone(), Some(Box::new("hello".to_string())), OdmaType::INTEGER, false, false) );
    println!("{:?}", OdmaPropertyImpl::new(qn.clone(), Some(Box::new("hello".to_string())), OdmaType::STRING, true, false) );

    let initial_values = vec!["hello".to_string(), "world".to_string()];
    let pi_string_mv = OdmaPropertyImpl::new(qn.clone(), Some(Box::new(initial_values)), OdmaType::STRING, true, false).unwrap();

    println!("{}", pi_string_mv.is_multi_value());
    println!("{}", pi_string_mv.is_read_only());
    let values = pi_string_mv.get_string_vec().unwrap();
    println!("Values: {:?}", values);

}

fn print_any_value<T: 'static + std::fmt::Debug>(any: &dyn std::any::Any) {
    match any.downcast_ref::<T>() {
        Some(val) => println!("{:?}", val),
        None => println!("None or wrong type"),
    }
}