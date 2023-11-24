package easylink.repository;

import easylink.entity.DeviceSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceSchemaRepository extends JpaRepository<DeviceSchema, Integer> {

}
