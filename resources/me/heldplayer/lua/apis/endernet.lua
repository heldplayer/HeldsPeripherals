local sides = { "bottom", "top", "left", "right", "front", "back" }
local colorNames = { "green", "brown", "black", "pink", "yellow", "orange", "magenta", "purple", "cyan", "red", "white", "lightBlue", "silver", "gray", "lime", "blue" }
local lastResult = nil

local function callBasic( ... )
  for index, side in ipairs( sides ) do
    if peripheral.isPresent( side ) and peripheral.getType( side ) == "TransWorldModem" then
      if peripheral.call( side, unpack(arg) ) then
        return true
      end
    end
  end
  
  return false
end

local function callAdvanced( ... )
  lastResult = nil
  
  for index, side in ipairs( sides ) do
    for index2, color in ipairs( colorNames ) do
      if peripheral.isPresent( side .. ":" .. color ) and peripheral.getType( side .. ":" .. color ) == "TransWorldModem" then
        if peripheral.call( side .. ":" .. color, unpack(arg) ) then
          lastResult = true
          return
        end
      end
    end
  end
  
  lastResult = false
end

function send( recipient, message, side )
  if recipient ~= nil and (type( recipient ) ~= "number" or recipient < 0) then
    error( "Positive number expected" )
  end
  if recipient == nil then
    error( "Positive number expected" )
  end
  if type( message ) ~= "string" then
    error( "String expected" )
  end
  if side ~= nil and type( side ) ~= "string" then
    error( "String expected" )
  end
  
  if side == nil then
    if not callBasic( "send", recipient, message ) then
      result, err = pcall( callAdvanced, "send", recipient, message )
      if result then
        return lastResult
      else
        return false
      end
    else
      return true
    end
  else
    return peripheral.call( side, "send", recipient, message )
  end
  
  return false
end

function transport( recipient, side )
  if recipient ~= nil and (type( recipient ) ~= "number" or recipient < 0) then
    error( "Positive number expected" )
  end
  if recipient == nil then
    error( "Positive number expected" )
  end
  if side ~= nil and type( side ) ~= "string" then
    error( "String expected" )
  end
  
  if side == nil then
    if not callBasic( "transport", recipient ) then
      result, err = pcall( callAdvanced, "transport", recipient )
      if result then
        return lastResult
      else
        return false
      end
    else
      return true
    end
  else
    return peripheral.call( side, "transport", recipient )
  end
  
  return false
end

function receive( nTimeout )
  local timer = nil
  if nTimeout then
    timer = os.startTimer( nTimeout )
  end
  while true do
    local e, p1, p2, p3 = os.pullEvent()
    
    if e == "transworld_receive" then
      return p1, p2, p3
    elseif e == "timer" and p1 == timer then
      return nil, nil, nil
    end
  end
end

function waitForItem( nTimeout )
  local timer = nil
  if nTimeout then
    timer = os.startTimer( nTimeout )
  end
  while true do
    local e, p1, p2 = os.pullEvent()
    
    if e == "transworld_item" then
      return p1, p2
    elseif e == "timer" and p1 == timer then
      return nil, nil
    end
  end
end

function getChargeLevel( side )
  if side ~= nil and type( side ) ~= "string" then
    error( "String expected" )
  end
  if side == nil then
    for index,value in ipairs(sides) do
      if peripheral.isPresent( value ) and peripheral.getType( value ) == "TransWorldModem" then
        return peripheral.call( value, "getChargeLevel" )
      end
    end
  else
    return peripheral.call( side, "getChargeLevel" )
  end
  
  return false
end

function inputOccupied( side )
  if side ~= nil and type( side ) ~= "string" then
    error( "String expected" )
  end
  if side == nil then
    for index,value in ipairs(sides) do
      if peripheral.isPresent( value ) and peripheral.getType( value ) == "TransWorldModem" then
        return peripheral.call( value, "getInputOccupied" )
      end
    end
  else
    return peripheral.call( side, "getInputOccupied" )
  end
  
  return false
end

function outputOccupied( side )
  if side ~= nil and type( side ) ~= "string" then
    error( "String expected" )
  end
  if side == nil then
    for index,value in ipairs(sides) do
      if peripheral.isPresent( value ) and peripheral.getType( value ) == "TransWorldModem" then
        return peripheral.call( value, "getOutputOccupied" )
      end
    end
  else
    return peripheral.call( side, "getOutputOccupied" )
  end
  
  return false
end