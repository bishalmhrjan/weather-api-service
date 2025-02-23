package com.weatherforcast.api.location;

import com.weatherapi.forecast.common.Location;
import com.weatherforcast.api.AbstractLocationService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LocationService extends AbstractLocationService {
    public LocationService(LocationRepository repository){
        super();
        this.locationRepository= repository;
    }
    public Location add(Location location){
        return locationRepository.save(location);
    }

    @Deprecated
    List<Location> list(){
        return locationRepository.findUntrashed();
    }

    @Deprecated
    public Page<Location> listByPage(int pageNum, int pageSize, String sortField, Map<String, Object> filterFields){
        Sort sort= Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNum, pageSize,sort);
        return locationRepository.findUntrashed(pageable);
    }

    private Sort createMultipleSorts(String sortOption){
        String [] sortFields = sortOption.split(",");
        Sort sort = null;

        if(sortFields.length>1){
            sort=createMultipleSorts(sortFields[0]);
            for(int i=0;i<sortFields.length;i++){
                sort.and(createSingleSort(sortFields[i]));
            }
        } else{
            sort= createSingleSort(sortOption);
        }
        return sort;
    }

    private Sort createSingleSort(String fieldName) {
        String actualFieldName= fieldName.replace("-","");
        return fieldName.startsWith("-")
                ? Sort.by(actualFieldName).descending() : Sort.by(actualFieldName).ascending();
    }

    public Location update(Location locationInRequest){
        String code = locationInRequest.getCode();
        Location locationInDb = locationRepository.findByCode(code);

        if(locationInDb ==null){
            throw  new LocationNotFoundException(code);
        }
        locationInDb.copyFieldsFrom(locationInRequest);
        return locationRepository.save(locationInDb);
    }


    public void delete(String code){
        Location location = locationRepository.findByCode(code);
        if(location == null){
            throw  new LocationNotFoundException(code);
        }
        locationRepository.trashByCode(code);
    }
}
