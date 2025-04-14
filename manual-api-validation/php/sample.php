<?php

require __DIR__ . '/vendor/autoload.php';

use OpenDMA\Api\OdmaQName;
use OpenDMA\Api\OdmaId;
use OpenDMA\Api\OdmaGuid;
use OpenDMA\Api\OdmaPropertyImpl;
use OpenDMA\Api\OdmaType;

echo "Welcome to OpenDMA API!\n";

// OdmaQName
echo "----OdmaQName----\n";

//$qn = new OdmaQName(null,"world");
//$qn = new OdmaQName("hello",null);
//$qn = new OdmaQName("","world");
//$qn = new OdmaQName("hello","");
$qn = new OdmaQName("hello","world");
echo $qn."\n";
$qn2 = new OdmaQName("Hello","world");
$qn3 = new OdmaQName("hello","World");
echo ($qn == $qn2 ? "true" : "false")."\n";
echo ($qn == $qn3 ? "true" : "false")."\n";
$qn4 = new OdmaQName("hello","world");
echo ($qn == $qn4 ? "true" : "false")."\n";

// OdmaId
echo "----OdmaId----\n";

//$id = new OdmaId("");
//$id = new OdmaId(null);
$id = new OdmaId("hello");
echo $id."\n";
$id2 = new OdmaId("Hello");
$id3 = new OdmaId("hello");
echo ($id == $id2 ? "true" : "false")."\n";
echo ($id == $id3 ? "true" : "false")."\n";

// OdmaGuid
echo "----OdmaGuid----\n";

//$guid = new OdmaGuid(null, $id);
//$guid = new OdmaGuid($id, null);
$guid = new OdmaGuid($id, $id);
echo $guid."\n";
$guid2 = new OdmaGuid(new OdmaId("hello"), new OdmaId("hello"));
$guid3 = new OdmaGuid(new OdmaId("Hello"), new OdmaId("hello"));
$guid4 = new OdmaGuid(new OdmaId("hello"), new OdmaId("Hello"));
echo ($guid == $guid2 ? "true" : "false")."\n";
echo ($guid == $guid3 ? "true" : "false")."\n";
echo ($guid == $guid4 ? "true" : "false")."\n";

// OdmaPropertyImpl
echo "----OdmaPropertyImpl----\n";

$piString = new OdmaPropertyImpl($qn, "hello", OdmaType::STRING, false, false);
$piInteger = new OdmaPropertyImpl($qn, 123, OdmaType::INTEGER, false, false);
$piDouble = new OdmaPropertyImpl($qn, 1.23, OdmaType::DOUBLE, false, false);
$piDate = new OdmaPropertyImpl($qn, new \DateTimeImmutable(), OdmaType::DATETIME, false, false);
echo $piString->getValue()."\n";
echo ($piString->isMultiValue() ? "true" : "false")."\n";
echo ($piString->isReadOnly() ? "true" : "false")."\n";
echo ($piString->isDirty() ? "true" : "false")."\n";
echo $piString->getString()."\n";
//echo $piString->getInteger()."\n";
echo $piInteger->getValue()."\n";
echo $piInteger->getInteger()."\n";
//echo $piInteger->getString()."\n";
echo $piDouble->getValue()."\n";
echo $piDate->getValue()->format('Y-m-d H:i:s')."\n";
echo ($piString->isDirty() ? "true" : "false")."\n";
$piString->setValue("world");
echo ($piString->isDirty() ? "true" : "false")."\n";
echo $piString->getValue()."\n";

$piStringRO = new OdmaPropertyImpl($qn, "hello", OdmaType::STRING, false, true);
echo $piStringRO->getValue()."\n";
echo ($piStringRO->isReadOnly() ? "true" : "false")."\n";
//$piStringRO->setValue("world");

//$piIntFailing = new OdmaPropertyImpl($qn, "hello", OdmaType::INTEGER, false, false);
//$piStringMVFailing = new OdmaPropertyImpl($qn, "hello", OdmaType::STRING, true, false);

$piStringMV = new OdmaPropertyImpl($qn, array(), OdmaType::STRING, true, false);
echo ($piStringMV->isMultiValue() ? "true" : "false")."\n";
echo ($piStringMV->isReadOnly() ? "true" : "false")."\n";
